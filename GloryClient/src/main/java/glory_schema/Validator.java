package glory_schema;

import javafx.scene.control.TextField;


public class Validator {
    public boolean isInputEmpty(TextField textField) {
        Boolean b= false;
        if (!(textField.getText() == null || textField.getText().length() == 0)) 
            b = true;
        return b;
    }
    public boolean isNumeric(String string){
        for (char c : string.toCharArray())
        {
            if (!Character.isDigit(c)) 
                return false;
        }
        return true;        
    }
}
