package com.jeeinho.github_api_practice;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GithubDao {
    private static GitHub gitHub;

    public GithubDao(String token) throws IOException {
        gitHub = new GitHubBuilder().withOAuthToken(token).build();
    }



    public void printAllRepositoriesByUserId(String id) throws IOException {
        getAllRepositoriesByUserId(id).keySet().forEach(System.out::println);
    }

    public void printContributionByUser(GHUser user) throws IOException {
        Collection<GHRepository> repositories = getAllRepositoriesByUser(user).values();
        List<GHRepository.Contributor> contributors = new ArrayList<>();

        System.out.println(user.getName() + "님의 오가니제이션 수 : " + user.getOrganizations().size());
        user.getOrganizations().forEach(organization -> {
            try {
                System.out.println("##" + organization.getName());
                organization.getRepositories().values().forEach(
                        repository -> {
                            System.out.println("###" + repository.getName());
                            try {
                                contributors.addAll(repository.listContributors().toList());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }});
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("##" + user.getName() + "님의 활동 (non Organization)");
        repositories.forEach(repository -> {
            System.out.println("###" + repository.getName());
            try {
                contributors.addAll(repository.listContributors().toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        AtomicInteger totalContribution = new AtomicInteger();
        contributors.forEach(contributor -> {
            try {
                if(contributor.getId() == user.getId()) {
                    System.out.println(contributor.getName() + " | " + contributor.getContributions());
                    totalContribution.addAndGet(contributor.getContributions());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(totalContribution.get());
    }

    public Map<String, GHRepository> getAllRepositoriesByUserId(String id) throws IOException {
        return getAllRepositoriesByUser(getUser(id));
    }

    public Map<String, GHRepository> getAllRepositoriesByUser(GHUser user) throws IOException {
        return user.getRepositories();
    }

    public GHUser getUser(String id) throws IOException {
        return gitHub.getUser(id);
    }
}
