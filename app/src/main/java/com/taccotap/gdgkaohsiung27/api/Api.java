package com.taccotap.gdgkaohsiung27.api;

import com.taccotap.gdgkaohsiung27.api.model.Repo;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface Api {

    // for example: https://api.github.com/orgs/reactivex/repos
    @GET("/orgs/{org}/repos")
    Observable<ArrayList<Repo>> getOrgRepos(@Path("org") String orgName);
}
