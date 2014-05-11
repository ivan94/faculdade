/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package david.ivan.rmi;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author ivan
 */
public class Main {
    
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Main main = new Main();
        
        int s = 6;
        Object os = s;
        
        Main.class.getMethod("m", os.getClass()).invoke(main, 5);
        
        
        System.out.println(os.getClass().getName());
        
    }
    
    public void m(int o){
        System.err.println(o);
    }
}
