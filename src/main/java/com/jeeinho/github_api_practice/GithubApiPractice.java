package com.jeeinho.github_api_practice;

public class GithubApiPractice {
    private static GithubDao githubDao;

    public static void main(String[] args) throws Exception {
        githubDao = new GithubDao(args[0]);
        String id = "jyeonjyan";

        githubDao.printContributionByUser(githubDao.getUser(id));
        System.out.println("테스트 종료");
    }
}
