package com.clovercard.clovertrainerutils.objects.requests;

import com.clovercard.clovertrainerutils.enums.RequestTags;

public class InteractRequest {
    private RequestTags tag;
    private String args;
    private int timeout;

    public InteractRequest(RequestTags tag, String args, int timeout) {
        this.tag = tag;
        this.args = args;
        this.timeout = timeout;
    }

    public RequestTags getTag() {
        return tag;
    }

    public void setTag(RequestTags tag) {
        this.tag = tag;
    }

    public String getArgs() {
        return args;
    }

    public String[] getSplitArgs() {
        return args.split(" ");
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
