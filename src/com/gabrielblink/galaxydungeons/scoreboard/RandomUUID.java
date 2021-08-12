 package com.gabrielblink.galaxydungeons.scoreboard;

import java.util.UUID;

class RandomUUID
{
  public String getUUID()
   {
    return 
       UUID.randomUUID().toString().substring(0, 6) + UUID.randomUUID().toString().substring(0, 6) + (int)Math.round(Math.random() * 100.0D);
   }
 }



