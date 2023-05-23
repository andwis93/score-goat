package com.restapi.scoregoat.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixtureParam {
   private int id;
   private String season;

   public FixtureParam(int id, String season) {
      this.id = id;
      this.season = season;
   }
}
