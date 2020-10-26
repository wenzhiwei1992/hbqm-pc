package com.smartpc.chiyun.config.dingding;

public class CorpUser {

    private static final long serialVersionUID = 1L;
    private String userid;
    private String name;
    private Boolean active;

    public CorpUser() {
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.active == null ? 0 : this.active.hashCode());
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        result = 31 * result + (this.userid == null ? 0 : this.userid.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CorpUser other = (CorpUser)obj;
            if (this.active == null) {
                if (other.active != null) {
                    return false;
                }
            } else if (!this.active.equals(other.active)) {
                return false;
            }

            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!this.name.equals(other.name)) {
                return false;
            }

            if (this.userid == null) {
                if (other.userid != null) {
                    return false;
                }
            } else if (!this.userid.equals(other.userid)) {
                return false;
            }

            return true;
        }
    }
}
