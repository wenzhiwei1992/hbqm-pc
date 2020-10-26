package com.smartpc.chiyun.config.dingding;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CorpUserDetail extends CorpUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tel;
    private String workPlace;
    private String remark;
    private String mobile;
    private String email;
    private String orderInDepts;
    private Boolean isAdmin;
    private Boolean isBoss;
    private Boolean isSenior;
    private String dingId;
    private String isLeaderInDepts;
    private Boolean isHide;
    private List<Long> department;
    private String position;
    private String avatar;
    private String jobnumber;
    private Map<String, String> extattr;

    public CorpUserDetail() {
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWorkPlace() {
        return this.workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderInDepts() {
        return this.orderInDepts;
    }

    public void setOrderInDepts(String orderInDepts) {
        this.orderInDepts = orderInDepts;
    }

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsBoss() {
        return this.isBoss;
    }

    public void setIsBoss(Boolean isBoss) {
        this.isBoss = isBoss;
    }

    public String getDingId() {
        return this.dingId;
    }

    public void setDingId(String dingId) {
        this.dingId = dingId;
    }

    public String getIsLeaderInDepts() {
        return this.isLeaderInDepts;
    }

    public void setIsLeaderInDepts(String isLeaderInDepts) {
        this.isLeaderInDepts = isLeaderInDepts;
    }

    public Boolean getIsHide() {
        return this.isHide;
    }

    public void setIsHide(Boolean isHide) {
        this.isHide = isHide;
    }

    public List<Long> getDepartment() {
        return this.department;
    }

    public void setDepartment(List<Long> department) {
        this.department = department;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getJobnumber() {
        return this.jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

    public Map<String, String> getExtattr() {
        return this.extattr;
    }

    public void setExtattr(Map<String, String> extattr) {
        this.extattr = extattr;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.avatar == null ? 0 : this.avatar.hashCode());
        result = 31 * result + (this.department == null ? 0 : this.department.hashCode());
        result = 31 * result + (this.dingId == null ? 0 : this.dingId.hashCode());
        result = 31 * result + (this.email == null ? 0 : this.email.hashCode());
        result = 31 * result + (this.extattr == null ? 0 : this.extattr.hashCode());
        result = 31 * result + (this.isAdmin == null ? 0 : this.isAdmin.hashCode());
        result = 31 * result + (this.isBoss == null ? 0 : this.isBoss.hashCode());
        result = 31 * result + (this.isHide == null ? 0 : this.isHide.hashCode());
        result = 31 * result + (this.isLeaderInDepts == null ? 0 : this.isLeaderInDepts.hashCode());
        result = 31 * result + (this.jobnumber == null ? 0 : this.jobnumber.hashCode());
        result = 31 * result + (this.mobile == null ? 0 : this.mobile.hashCode());
        result = 31 * result + (this.orderInDepts == null ? 0 : this.orderInDepts.hashCode());
        result = 31 * result + (this.position == null ? 0 : this.position.hashCode());
        result = 31 * result + (this.remark == null ? 0 : this.remark.hashCode());
        result = 31 * result + (this.tel == null ? 0 : this.tel.hashCode());
        result = 31 * result + (this.workPlace == null ? 0 : this.workPlace.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CorpUserDetail other = (CorpUserDetail) obj;
            if (this.avatar == null) {
                if (other.avatar != null) {
                    return false;
                }
            } else if (!this.avatar.equals(other.avatar)) {
                return false;
            }

            if (this.department == null) {
                if (other.department != null) {
                    return false;
                }
            } else if (!this.department.equals(other.department)) {
                return false;
            }

            if (this.dingId == null) {
                if (other.dingId != null) {
                    return false;
                }
            } else if (!this.dingId.equals(other.dingId)) {
                return false;
            }

            if (this.email == null) {
                if (other.email != null) {
                    return false;
                }
            } else if (!this.email.equals(other.email)) {
                return false;
            }

            if (this.extattr == null) {
                if (other.extattr != null) {
                    return false;
                }
            } else if (!this.extattr.equals(other.extattr)) {
                return false;
            }

            if (this.isAdmin == null) {
                if (other.isAdmin != null) {
                    return false;
                }
            } else if (!this.isAdmin.equals(other.isAdmin)) {
                return false;
            }

            if (this.isBoss == null) {
                if (other.isBoss != null) {
                    return false;
                }
            } else if (!this.isBoss.equals(other.isBoss)) {
                return false;
            }

            if (this.isHide == null) {
                if (other.isHide != null) {
                    return false;
                }
            } else if (!this.isHide.equals(other.isHide)) {
                return false;
            }

            if (this.isLeaderInDepts == null) {
                if (other.isLeaderInDepts != null) {
                    return false;
                }
            } else if (!this.isLeaderInDepts.equals(other.isLeaderInDepts)) {
                return false;
            }

            if (this.jobnumber == null) {
                if (other.jobnumber != null) {
                    return false;
                }
            } else if (!this.jobnumber.equals(other.jobnumber)) {
                return false;
            }

            if (this.mobile == null) {
                if (other.mobile != null) {
                    return false;
                }
            } else if (!this.mobile.equals(other.mobile)) {
                return false;
            }

            if (this.orderInDepts == null) {
                if (other.orderInDepts != null) {
                    return false;
                }
            } else if (!this.orderInDepts.equals(other.orderInDepts)) {
                return false;
            }

            if (this.position == null) {
                if (other.position != null) {
                    return false;
                }
            } else if (!this.position.equals(other.position)) {
                return false;
            }

            if (this.remark == null) {
                if (other.remark != null) {
                    return false;
                }
            } else if (!this.remark.equals(other.remark)) {
                return false;
            }

            if (this.tel == null) {
                if (other.tel != null) {
                    return false;
                }
            } else if (!this.tel.equals(other.tel)) {
                return false;
            }

            if (this.workPlace == null) {
                if (other.workPlace != null) {
                    return false;
                }
            } else if (!this.workPlace.equals(other.workPlace)) {
                return false;
            }

            return true;
        }
    }

    public Boolean getSenior() {
        return this.isSenior;
    }

    public void setSenior(Boolean senior) {
        this.isSenior = senior;
    }
}
