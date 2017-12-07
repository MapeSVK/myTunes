/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.Model;

/**
 *
 * @author sebok
 */
public class ModelException extends Exception
{

    public ModelException(String message)
    {
        super(message);
    }

    public ModelException(Exception ex)
    {
        super(ex.getMessage());
    }
    
    @Override
    public String getMessage()
    {
        return super.getMessage(); //To change body of generated methods, choose Tools | Templates.
    }
       
}
