
import org.netbeans.jemmy.*;
import org.netbeans.jemmy.operators.*;

public class FindComponentsSample implements Scenario {
	public int runIt(Object param) {
	try {
		Bundle bundle = new Bundle();
		bundle.loadFromFile(System.getProperty("user.dir") +
				System.getProperty("file.separator") +
				"resourcesample.txt");

		new ClassReference(bundle.getResource("guibrowser.main_class")).
		startApplication();

		JFrameOperator mainFrame = 
		new JFrameOperator(bundle.getResource("guibrowser.main_window"));

		new JMenuBarOperator(mainFrame).
		pushMenuNoBlock(bundle.getResource("guibrowser.show_properties_menu"), 
				"|");

		new JDialogOperator(mainFrame, 
				bundle.getResource("guibrowser.properties_window"));
	} catch(Exception e) {
		e.printStackTrace();
		return(1);
	}
	return(0);
	}
	
	
	public static void main(String[] argv) {
	JemmyProperties.getCurrentTimeouts().setTimeout("Test.WholeTestTimeout", 60000);
	try {
		JemmyProperties.getCurrentTimeouts().load("times.txt");
	} catch (Exception e) {
		e.printStackTrace();
	} 
	String[] params = {"FindComponentsSample"};
	org.netbeans.jemmy.Test.main(params);
	}
}