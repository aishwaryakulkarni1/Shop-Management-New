package com.inevitablesol.www.shopmanagement.DateFormat;

/**
 * Created by Pritam on 16-02-2018.
 */

public class ParseDate
{
      public  static  String  geDate(String date)
      {
          if(date.isEmpty() || date==null)
          {
              return "No date";
          }
           return date.substring(0,10);
      }
}
