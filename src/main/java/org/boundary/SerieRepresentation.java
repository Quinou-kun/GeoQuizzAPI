package org.boundary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.entity.Photo;
import org.entity.Serie;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.provider.Secured;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("series")
public class SerieRepresentation<FormContentDisposition> {

    @Inject
    SerieResource serieResource;

    @Inject
    PhotoResource photoResource;
    
    @Context
    UriInfo uriInfo;

    /**
     * 
     *  Route permettant de récupérer la liste des séries
     * 
     */

    @GET
    public Response getSeries() 
    {
        JsonObject json = Json.createObjectBuilder()
                .add("series", buildSeriesArray())
                .build();
        return Response.ok(json).build();
    }

    private JsonValue buildSeriesArray() 
    {
        List<Serie> series = serieResource.findAll();

        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(Serie s : series)
        {
            jab.add(buildSerieJson(s));
        }

		return jab.build();
	}

    private JsonValue buildSerieJson(Serie s) 
    {
        JsonValue json = Json.createObjectBuilder()
            .add("id", s.getId())
            .add("ville", s.getVille())
            .add("mapOptions", s.getMapOptions())
            .add("photos", buildJsonForPhotos(s))
            .build();

		return json;
    }
    
    private JsonValue buildJsonForPhotos(Serie s) 
    {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(Photo p : s.getPhotos())
        {
            jab.add(buildJsonForPhoto(p));
        }

        return jab.build();
	}

    private JsonValue buildJsonForPhoto(Photo p) 
    {
        JsonValue json = Json.createObjectBuilder()
            .add("id", p.getId())
            .add("desc", p.getDescription())
            .add("position", p.getPosition())
            .add("url", p.getUrl())
            .build();

		return json;
	}

	/**
     * 
     *  Route permettant de récupérer une série
     * 
     */

    @GET
    @Path("{id}")
    public Response getSerie(@PathParam("id") String id, @QueryParam("size") int size) 
    {
        Serie s = serieResource.findById(id);

        if(s == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(buildSerieRandomJson(s, size)).build();
    }

    private JsonValue buildSerieRandomJson(Serie s, int size) 
    {
        JsonValue json = Json.createObjectBuilder()
            .add("id", s.getId())
            .add("ville", s.getVille())
            .add("mapOptions", s.getMapOptions())
            .add("photos", buildJsonForRandomPhotos(s, size))
            .build();

		return json;
    }

    private JsonValue buildJsonForRandomPhotos(Serie s, int size) 
    {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        List<Photo> photos = s.getPhotos();
        Collections.shuffle(photos);

        int limit = (size > 0) ? size : 10;

        for(int i = 0 ; (i < photos.size() && i < limit) ; i++)
        {
            jab.add(buildJsonForPhoto(photos.get(i)));
        }

        return jab.build();
	}

     /**
     * 
     *  Route permettant d'ajouter une série
     * 
     */

    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSerie(@Valid Serie serie)
    {
        serie.setId(UUID.randomUUID().toString());
        serieResource.save(serie);

        URI uri = uriInfo.getBaseUriBuilder().path("series/" + serie.getId()).build();

        return Response.created(uri).build();
    }

     /**
     * 
     *  Route permettant d'ajouter une photo à une série
     * 
     */
     
    @POST
    @Secured
    @Path("{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPhotoToSerie(@PathParam("id") String id, 
                                    @DefaultValue("") @QueryParam("desc") String desc, 
                                    @DefaultValue("") @QueryParam("pos") String pos,
                                    MultipartFormDataInput input)
    {
        Serie serie = serieResource.findById(id);
        if(serie == null)
            return Response.status(Response.Status.NOT_FOUND).build();        

        Map<String, List<InputPart>> formulaire = input.getFormDataMap();
        List<InputPart> inputParts = formulaire.get("file");
        
        Photo photo = new Photo();
        photo.setId(UUID.randomUUID().toString());

        for (InputPart ip : inputParts) 
        {
            MultivaluedMap<String, String> headers = ip.getHeaders();
            String filename = photo.getId();

            // On regarde le type de fichier
            String[] contentTypeHeader = headers.getFirst("Content-Type").split(";");
        
            String format = "";
            for(String s : contentTypeHeader)
            {
                if(! (s.contains("image/jpeg") || s.contains("image/png") || s.contains("image/gif") || s.contains("image/bmp")) ) 
                {
                    return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).build();
                }

                if(s.contains("image/jpeg")) format = ".jpg";
                if(s.contains("image/png")) format = ".png";
                if(s.contains("image/gif")) format = ".gif";
                if(s.contains("image/bmp")) format = ".bmp";
            }
            
            try 
            {
                InputStream is = ip.getBody(InputStream.class,null);
                byte[] bytes = SerieRepresentation.toByteArray(is);
                writeFile(bytes,"/opt/jboss/wildfly/standalone/tmp/" + filename + format);

                photo.setUrl("/opt/jboss/wildfly/standalone/tmp/"+filename + format);
            } 
            catch (IOException ioe) 
            {
                ioe.printStackTrace();
            }
        }

        photo.setDescription(desc);
        photo.setPosition(pos);
        photo.setSerie(serie);
        photoResource.save(photo);

        URI uri = uriInfo.getBaseUriBuilder().path("photos/" + serie.getId()).build();

        return Response.status(200).location(uri).build();
    }

    public static byte[] toByteArray(InputStream is) throws IOException 
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try 
        {
            byte[] b = new byte[4096];
            int n = 0;
            while ((n = is.read(b)) != -1) 
            {
                output.write(b, 0, n);
            }
            return output.toByteArray();
        } 
        finally 
        {
            output.close();
        }
    }
    
    private void writeFile(byte[] contenu, String filename) throws IOException 
    {    
        File file = new File(filename);
        
        FileOutputStream fop = new FileOutputStream(file);

        fop.write(contenu);
        fop.flush();
        fop.close();
    }

    /*
    private String getFileName(MultivaluedMap<String, String> headers) 
    {
        String[] contenuHeader = headers.getFirst("Content-Disposition").split(";");

        for (String filename : contenuHeader) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }            
        }
        
        return "inconnu";
    }
    */
}
