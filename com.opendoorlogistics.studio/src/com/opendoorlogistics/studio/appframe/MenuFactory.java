package com.opendoorlogistics.studio.appframe;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import com.opendoorlogistics.api.HasApi;
import com.opendoorlogistics.core.scripts.elements.Script;
import com.opendoorlogistics.core.tables.io.SupportedFileType;
import com.opendoorlogistics.studio.dialogs.AboutBoxDialog;
import com.opendoorlogistics.studio.internalframes.HasInternalFrames.FramePlacement;
import com.opendoorlogistics.studio.panels.FunctionsListPanel;
import com.opendoorlogistics.studio.scripts.editor.ScriptWizardActions;
import com.opendoorlogistics.studio.scripts.editor.ScriptWizardActions.WizardActionsCallback;
import com.opendoorlogistics.studio.scripts.execution.ScriptUIManager;
import com.opendoorlogistics.utils.ui.ODLAction;

public class MenuFactory {

	public JMenu createHelpMenu(ActionFactory actionBuilder, AbstractAppFrame appFrame) {
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');

		Action helpsite = actionBuilder.createGotoWebsiteAction(appFrame);
		if (helpsite != null) {
			mnHelp.add(helpsite);
		}

		mnHelp.add(new AbstractAction("About ODL Studio") {

			@Override
			public void actionPerformed(ActionEvent e) {
				final AboutBoxDialog dlg = new AboutBoxDialog(appFrame, false);
				dlg.setLocationRelativeTo(appFrame);
				dlg.setVisible(true);
			}
		});

		mnHelp.add(new AbstractAction("List of data adapter functions") {

			@Override
			public void actionPerformed(ActionEvent e) {
				appFrame.addInternalFrame(FunctionsListPanel.createFrame(), FramePlacement.AUTOMATIC);
			}
		});

		mnHelp.add(new AbstractAction("List of 3rd party data & libraries") {

			@Override
			public void actionPerformed(ActionEvent e) {
				final AboutBoxDialog dlg = new AboutBoxDialog(appFrame, true);
				dlg.setTitle("3rd party data & libs used in ODL Studio");
				dlg.setLocationRelativeTo(appFrame);
				dlg.setVisible(true);
			}
		});
		return mnHelp;
	}

	public JMenu createWindowsMenu(DesktopAppFrame appFrame) {
		JMenu mnWindow = new JMenu("Window");
		mnWindow.setMnemonic('W');

		mnWindow.add(new AbstractAction("Tile open windows") {

			@Override
			public void actionPerformed(ActionEvent e) {
				appFrame.tileWindows();
			}
		}).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
		mnWindow.add(new AbstractAction("Cascade open windows") {

			@Override
			public void actionPerformed(ActionEvent e) {
				appFrame.cascadeWindows();
			}
		}).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));

		mnWindow.add(new AbstractAction("Close all open windows") {

			@Override
			public void actionPerformed(ActionEvent e) {
				appFrame.closeWindows();
			}
		});

		mnWindow.add(new AbstractAction("Minimise all open windows") {

			@Override
			public void actionPerformed(ActionEvent e) {
				appFrame.minimiseWindows();
			}
		}).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK));

		return mnWindow;

	}
	
	public JMenu createImportMenu(AbstractAppFrame appFrame){
		JMenu mnImport = new JMenu("Import");

		class ImportPair {
			String menuString;
			SupportedFileType type;

			public ImportPair(String menuString, SupportedFileType type) {
				super();
				this.menuString = menuString;
				this.type = type;
			}
		}
		for (final ImportPair type : new ImportPair[] { new ImportPair("Comma separated (CSV) text", SupportedFileType.CSV), new ImportPair("Tab separated text", SupportedFileType.TAB), new ImportPair("Excel", SupportedFileType.EXCEL),
				new ImportPair("Shapefile (link geometry to original file)", SupportedFileType.SHAPEFILE_LINKED_GEOM), new ImportPair("Shapefile (copy geometry into spreadsheet)", SupportedFileType.SHAPEFILE_COPIED_GEOM), }) {
			mnImport.add(new AbstractAction(type.menuString) {

				@Override
				public void actionPerformed(ActionEvent e) {
					appFrame.importFile(type.type);
				}
			});
		}
		
		return mnImport;
				
	}
	
	public JMenu createScriptCreationMenu(AbstractAppFrame appFrame, ScriptUIManager scriptManager) {
		JMenu mnCreateScript = new JMenu("Create script");
		mnCreateScript.setMnemonic('C');
		for (ODLAction action : new ScriptWizardActions(appFrame.getApi(), appFrame, scriptManager.getAvailableFieldsQuery()).createComponentActions(new WizardActionsCallback() {

			@Override
			public void onNewScript(Script script) {
				scriptManager.launchScriptEditor(script, null, null);
			}
		})) {
			mnCreateScript.add(action);
		}
		return mnCreateScript;
	}

}
