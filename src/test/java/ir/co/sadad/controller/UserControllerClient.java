package ir.co.sadad.controller;

import ir.co.sadad.controller.vm.ManagedUserVM;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterRestClient
@Path("/api")
public interface UserControllerClient {

    @POST
    @Path(value = "/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(ManagedUserVM managedUserVM);

    @PUT
    @Path(value = "/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(ManagedUserVM managedUserVM);

    @GET
    @Path(value = "/users/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("login") String login);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/users")
    public Response getAllUsers(@QueryParam("page") int page, @QueryParam("size") int size);

    @GET
    @Path("/users/authorities")
    public List<String> getAuthorities();

    @DELETE
    @Path(value = "/users/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("login") String login);

}
