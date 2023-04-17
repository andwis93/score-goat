package com.restapi.scoregoat.domain;

import com.restapi.scoregoat.domain.client.TimeFrame.TimeFrame;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class FixtureParam {
   private int id;
   private String season;
   private LocalDate fromDate;
   private LocalDate toDate;

   public FixtureParam(int id, String season) {
      this.id = id;
      this.season = season;
      this.fromDate = LocalDate.now().minusDays(TimeFrame.DAYS_BEFORE.getTimeFrame());
      this.toDate = LocalDate.now().plusDays(TimeFrame.DAYS_AFTER.getTimeFrame());
   }
}
