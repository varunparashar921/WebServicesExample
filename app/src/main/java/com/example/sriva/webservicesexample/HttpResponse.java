package com.example.sriva.webservicesexample;

/**
 * Created by sriva on 23.1.17.
 */
public class HttpResponse {
    private StatusLine statusLine;
    private HttpEntity entity;

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public HttpEntity getEntity() {
        return entity;
    }
}
