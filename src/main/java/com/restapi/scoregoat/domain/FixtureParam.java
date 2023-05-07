package com.restapi.scoregoat.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixtureParam {
   private int id;
   private String season;
 //  private LocalDate fromDate;
  // private LocalDate toDate;

   public FixtureParam(int id, String season) {
      this.id = id;
      this.season = season;
    //  this.fromDate = LocalDate.now().minusDays(TimeFrame.DAYS_BEFORE.getTimeFrame());
   //   this.toDate = LocalDate.now().plusDays(TimeFrame.DAYS_AFTER.getTimeFrame());
   }
}
