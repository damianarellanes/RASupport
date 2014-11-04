package RASupport.rasupport.rasupportconfig.random;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 * RASupportConfig: random manager in the support
 * @author damianarellanes
 */
public class RASupportRandomManager {
    
    public static Set<Integer> getNonequalRandomNumbers(int requiredNumbers) {
        Random rng = new Random();
        Set<Integer> generated = new LinkedHashSet<>();
        while (generated.size() < requiredNumbers)
        {
            Integer next = rng.nextInt(requiredNumbers);
            // As we're adding to a set, this will automatically do a containment check
            generated.add(next);                        
        }
        return generated;
    }
    
    public static int getRandomInt(int min, int max) {
        
        return new Random().nextInt((max - min)) + min;
    }
    
    public static double getRandomFloat(double min, double max) {
        //return new Random().nextInt((max - min) + 1) + min;
        double number = Math.random() < 0.5 ? ((1-Math.random()) * (max-min) + min) : (Math.random() * (max-min) + min);        

        number = Math.round(number * 100);
        return number/100;
    }
    
}
