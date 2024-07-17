package net.andreabattista.InOutFlow.business;

public class MainSmartCard {
    
    public static void main(String[] args) {
        SmartCardReader reader = new SmartCardReader();
        Thread readerThread = new Thread(reader);
        readerThread.start();
        
        // Assicurati di poter fermare il thread quando l'applicazione si chiude
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            reader.stop();
            try {
                readerThread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }));
    }
}
