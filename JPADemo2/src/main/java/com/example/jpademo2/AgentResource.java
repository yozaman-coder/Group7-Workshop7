package com.example.jpademo2;

import com.example.model.Agent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;

@Path("/agent")
public class AgentResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAgents()
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT a FROM Agent a");
        List<Agent> list = query.getResultList();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Agent>>(){}.getType();
        entityManager.close();
        return gson.toJson(list, type);
    }
    @GET
    @Path("/getagent/{agentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAgent(@PathParam("agentId") int agentId)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Agent agent = entityManager.find(Agent.class, agentId);

        Gson gson = new Gson();
        entityManager.close();
        return gson.toJson(agent);
    }
    @POST
    @Path("/updateagent/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String postAgent(String jsonString)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();


        Gson gson = new Gson();
        Agent agent = gson.fromJson(jsonString, Agent.class);
        entityManager.getTransaction().begin();
        Agent mergedAgent = entityManager.merge(agent);
        if(mergedAgent != null)
        {
            entityManager.getTransaction().commit();
            entityManager.close();
            return "{ 'message':'Update successful' }";
        }
        else
        {
            entityManager.getTransaction().rollback();
            entityManager.close();
            return "{ 'message':'Update failed' }";
        }
    }
    @PUT
    @Path("/putagent/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String putAgent(String jsonString) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();
        Agent agent = gson.fromJson(jsonString, Agent.class);
        entityManager.getTransaction().begin();
        entityManager.persist(agent);
        if(entityManager.contains(agent))
        {
            entityManager.getTransaction().commit();
            entityManager.close();
            return "{ 'message':'Insert successful' }";
        }
        else
        {
            entityManager.getTransaction().rollback();
            entityManager.close();
            return "{ 'message':'Insert failed' }";
        }
    }
    @DELETE
    @Path("/deleteagent/{ agentId }")
    @Produces(MediaType.APPLICATION_JSON)
    public String postAgent(@PathParam("agentId") int agentId)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Agent agent = entityManager.find(Agent.class, agentId);

        Gson gson = new Gson();
        entityManager.getTransaction().begin();
        entityManager.remove(agent);
        if(!entityManager.contains(agent))
        {
            entityManager.getTransaction().commit();
            entityManager.close();
            return "{ 'message':'Delete successful' }";
        }
        else
        {
            entityManager.getTransaction().rollback();
            entityManager.close();
            return "{ 'message':'Delete failed' }";
        }
    }
}