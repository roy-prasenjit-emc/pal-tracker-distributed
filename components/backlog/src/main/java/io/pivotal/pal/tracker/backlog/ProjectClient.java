package io.pivotal.pal.tracker.backlog;

import org.springframework.web.client.RestOperations;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {


    private final Map<Long, ProjectInfo> projectsCache = new ConcurrentHashMap<>();
    private final RestOperations restOperations;
    private final String endpoint;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo project = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);

        projectsCache.put(projectId, project);

        return project;
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        return projectsCache.get(projectId);
    }
}
