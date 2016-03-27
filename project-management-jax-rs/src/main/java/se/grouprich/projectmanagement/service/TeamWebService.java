package se.grouprich.projectmanagement.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import se.grouprich.projectmanagement.Loader;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.exception.TeamException;
import se.grouprich.projectmanagement.model.Team;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.UserData;
import se.grouprich.projectmanagement.model.mapper.TeamMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.Response.Status;

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamWebService
{
	private static final TeamService teamService = Loader.getBean(TeamService.class);
	private static final UserService userService = Loader.getBean(UserService.class);
	private static final TeamMapper teamMapper = new TeamMapper();

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createTeam(Team team)
	{
		TeamData createdTeam = teamService.createOrUpdate(teamMapper.convertTeamToTeamData(team));
		URI location = uriInfo.getAbsolutePathBuilder().path(getClass(), "getTeam").build(createdTeam.getId());

		return Response.created(location).build();
	}

	@GET
	@Path("{id}")
	public Response getTeam(@PathParam("id") Long id)
	{
		TeamData teamData = teamService.findById(id);
		System.out.println(teamData);
		Team team = teamMapper.convertTeamDataToTeam(teamData);
		team.setStatus(teamData.getStatus().toString());

		return Response.ok(team).build();
	}

	@PUT
	@Path("{id}")
	public Response updateTeam(@PathParam("id") Long id, Team team)
	{
		TeamData teamData = teamService.findById(id);

		if (teamData == null)
		{
			Response.status(Status.NOT_FOUND);
		}

		TeamData updateTeamData = teamMapper.updateTeamData(team, teamData);
		teamService.createOrUpdate(updateTeamData);

		return Response.noContent().build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteTeam(@PathParam("id") Long id)
	{
		if (teamService.findById(id) == null)
		{
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		teamService.deleteById(id);

		return Response.noContent().build();
	}

	@GET
	public Response getAllTeams()
	{
		Iterable<TeamData> teamDataIterable = teamService.findAll();
		if (Iterables.isEmpty(teamDataIterable))
		{
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		List<TeamData> teamDataList = Lists.newArrayList(teamDataIterable);
		GenericEntity<List<Team>> teams = teamMapper.convertList(teamDataList);

		return Response.ok(teams).build();
	}

	@PUT
	@Path("{teamId}/user/{userId}")
	public Response addUserToTeam(@PathParam("teamId") Long teamId, @PathParam("userId") Long userId) throws TeamException, RepositoryException
	{
		TeamData teamData = teamService.findById(teamId);
		UserData userData = userService.findById(userId);
		if (teamData == null || userData == null)
		{
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		teamService.addUserToTeam(teamData, userData);

		return Response.noContent().build();
	}
}
