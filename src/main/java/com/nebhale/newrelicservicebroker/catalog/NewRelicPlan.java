package com.nebhale.newrelicservicebroker.catalog;

import java.util.UUID;


public class NewRelicPlan {
  private String planName = null;
  private String licenseKey = null;
  private String planId = null;


  public String getPlanName()
  {
    return this.planName;
  }

  public void setPlanName(String planName) {
    this.planName = planName;
  }

  public String getLicenseKey() {
    return this.licenseKey;
  }

  public void setLicenseKey(String licenseKey) {
    this.licenseKey = licenseKey;
  }

  public String getPlanId() {
    return this.planId;
  }

  public void setPlanId() throws Exception {
    if ((this.planName == null) || (this.licenseKey == null)) {
      throw new Exception("No null planName or licenseKey allowed");
    }

    if (this.planId != null)
      return;

    this.planId = UUID.nameUUIDFromBytes((this.planName + this.licenseKey).getBytes()).toString();
  }

  public String toString()
  {
    return "Plan [planName=" + this.planName + ", licenseKey=" + this.licenseKey + ", planId=" + this.planId + "]";
  }
}

