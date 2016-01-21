package com.taccotap.gdgkaohsiung27.api.model;

import com.google.gson.annotations.SerializedName;

// Model class for Gson deserilization
public class Repo {
    public String id;
    public String name;

    @SerializedName("full_name")
    public String fullName;

    @SerializedName("html_url")
    public String htmlUrl;

    public String description;

    @SerializedName("stargazers_count")
    public int stargazersCount;

    @SerializedName("watchers_count")
    public int watchersCount;
}
