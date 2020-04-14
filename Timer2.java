Timer timer = new Timer();
timer.clockListener(this);
timer.start();
	   
public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("clockRun")) {
        Timer timer = (Timer) e.getSource();
        ui.run(timer.timeDisplay());
    }
}
   
public void run(String time) {
    if (timerLabel != null)
        timerLabel.setText(time);
}

class Timer extends Thread {
   public static final int INTERVAL = 1000;
   private boolean running;
   private int minutes;
   private int seconds;
   private ActionListener run;

   Timer() {
      minutes = 0;
      seconds = 0;
      running = true;
   }
   
   @Override
   public void run() {
      while (true) {
         doNothing();

         if (running) {
            seconds++;
            
            if(seconds > 59)
            {
               seconds = 0;
               minutes++;
            }
         }
         
         this.timeActionEvent();
      }
   }

   public void toggle() {
      this.running = !this.running;
   }

   private void timeActionEvent() {
    if (this.run != null) {
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clockRun");
        this.run.actionPerformed(e);
        }
   }
   
   public void clockListener(ActionListener a) {
    this.run = a;
   }
   
   public final int getMinutes() {
    return minutes;
   }
   
   public final int getSeconds() {
    return seconds;
   }
   
   public String timeDisplay() {
    return getMinutes() + ":" + getSeconds();
   }
   
   public void doNothing() {
    try {
        Thread.sleep(INTERVAL);
    } catch (InterruptedException e) {
        System.out.println("Unexpected Interrupt");
        System.exit(0);
    }
}