class Timer extends Thread
{
   public static final int PAUSE = 1000;
   public int minute;
   public int second;
   public JLabel timeLabel;
   
   Timer()
   {
      minute = 0;
      second = 0;
      timeLabel = new JLabel("00:00");
   }
   
   public void run()
   {
      while(true)
      {
         doNothing(PAUSE);
         second++;
         
         if(second > 59)
         {
            second = 0;
            minute++;
         }
         System.out.println(this.getMinute() + " : " + this.getSecond());
      }
   }
   
   public final int getMinute()
   {
      return minute;
   }
   
   public final int getSecond()
   {
      return second;
   }
   
   public void doNothing(int millisecond)
   {
      try
      {
         Thread.sleep(millisecond);
      }
      catch(InterruptedException e)
      {
         System.out.println("Unexpected Interrupt");
         System.exit(0);
      }
   }
}