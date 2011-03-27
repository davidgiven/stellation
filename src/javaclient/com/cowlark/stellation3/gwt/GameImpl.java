package com.cowlark.stellation3.gwt;

import java.util.HashMap;
import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.controllers.LabelController;
import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.controllers.MarkupHandler;
import com.cowlark.stellation3.common.controllers.ObjectSummaryController;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.controllers.StarMapStarHandler;
import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.cowlark.stellation3.common.database.Transport;
import com.cowlark.stellation3.common.game.CompletionListener;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.gwt.controllers.ButtonsControllerImpl;
import com.cowlark.stellation3.gwt.controllers.GroupTitleControllerImpl;
import com.cowlark.stellation3.gwt.controllers.MarkupControllerImpl;
import com.cowlark.stellation3.gwt.controllers.ObjectSummaryControllerImpl;
import com.cowlark.stellation3.gwt.controllers.StarMapImpl;
import com.cowlark.stellation3.gwt.controllers.StarMapStarControllerImpl;
import com.cowlark.stellation3.gwt.controllers.TextFieldControllerImpl;
import com.cowlark.stellation3.gwt.ui.CanvasResources;
import com.cowlark.stellation3.gwt.ui.ClockWidget;
import com.cowlark.stellation3.gwt.ui.DialogueImpl;
import com.cowlark.stellation3.gwt.ui.RefreshWidget;
import com.cowlark.stellation3.gwt.ui.Screen;
import com.cowlark.stellation3.gwt.ui.TabbedPane;
import com.cowlark.stellation3.gwt.ui.TabbedPaneContainer;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GameImpl extends Game
{
	public static GameImpl Instance;
	
	public Screen Screen;
	private HashMap<PaneAspect, Pane> _panes;
	private DialogBox _progress;
	private StarMapImpl _starmap;
	
	public GameImpl()
    {
		Instance = this;
		Screen = new Screen();
		_panes = new HashMap<PaneAspect, Pane>();
		RootLayoutPanel.get().add(Screen);
    }
	
	@Override
	public void loginCompleted(int playeroid)
	{
	    super.loginCompleted(playeroid);
	    
	    ClockWidget clock = new ClockWidget();
	    Screen.ClockContainer.add(clock);
	    
	    RefreshWidget meta = new RefreshWidget();
	    Screen.MetaContainer.add(meta);
	}
	
	@Override
	public void loadUIData(CompletionListener listener)
	{
		CanvasResources.waitForLoad(listener);
	}
	
	@Override
	public void showProgress(String message)
	{
		if (_progress != null)
		{
			_progress.hide();
			_progress = null;
		}
		
		if (message != null)
		{
			_progress = new DialogBox();
			_progress.setText(message);
			_progress.center();
			_progress.show();
		}
	}
	
	private void replacePane(PaneAspect aspect, Pane pane)
	{
		Pane oldpane = _panes.get(aspect);
		if (oldpane != null)
			oldpane.closePane();
		
		_panes.put(aspect, pane);
	}
	
	private void placePaneInTab(TabbedPane pane, LayoutPanel container)
	{
		TabbedPaneContainer c;
		if (container.getWidgetCount() == 0)
		{
			c = new TabbedPaneContainer();
			container.add(c);
		}
		else
			c = (TabbedPaneContainer) container.getWidget(0);
		c.add(pane);
	}
	
	@Override
	public Pane showPane(PaneAspect aspect, PaneHandler ph, String title)
	{
		switch (aspect)
		{
			case LOGIN:
			{
				DialogueImpl d = new DialogueImpl(ph, title);
				d.show();
				return d;
			}
				
			case STARMAP:
			{
				_starmap = new StarMapImpl(ph);
				replacePane(aspect, _starmap);
				return _starmap;
			}
			
			case SUMMARY:
			{
				TabbedPane pane = new TabbedPane(aspect, ph, title);
				replacePane(aspect, pane);
				placePaneInTab(pane, Screen.LeftContainer);
				return pane;
			}
			
			case LOCATION:
			{
				TabbedPane pane = new TabbedPane(aspect, ph, title);
				replacePane(aspect, pane);
				placePaneInTab(pane, Screen.MiddleContainer);
				return pane;
			}
			
			case CONTROLPANEL:
			{
				TabbedPane pane = new TabbedPane(aspect, ph, title);
				replacePane(aspect, pane);
				placePaneInTab(pane, Screen.RightContainer);
				return pane;
			}
			
			default:
				assert false;
		}
		
		return null;
	}
	
//	@Override
//	public StarMap createStarMap()
//	{
//		StarMapImpl sm = new StarMapImpl();
//		_screen.setStarmap(sm);
//	    return sm;
//	}
	
	@Override
	public Transport createTransport()
	{
		return new TransportImpl(S.CGIURL);
	}
	
	@Override
	public GroupTitleController createGroupTitleController(String title)
	{
	    return new GroupTitleControllerImpl(title);
	}
	
	@Override
	public LabelController createLabelController(String label)
	{
	    return new MarkupControllerImpl(null, label);
	}
	
	@Override
	public MarkupController createMarkupController(MarkupHandler mh,
	        String label)
	{
	    return new MarkupControllerImpl(mh, label);
	}
	
	@Override
	public TextFieldController createTextFieldController(
			TextFieldHandler tfh, String label)
	{
		return new TextFieldControllerImpl(tfh, label, false);
	}
	
	@Override
	public TextFieldController createPasswordTextFieldController(
			TextFieldHandler tfh, String label)
	{
		return new TextFieldControllerImpl(tfh, label, true);
	}
	
	@Override
	public ButtonsController createButtonsController(
	        ButtonsHandler bh, String... strings)
	{
	    return new ButtonsControllerImpl(bh, strings);
	}
	
	@Override
	public StarMapStarController createStarMapStarController(
	        StarMapStarHandler smsh, StarMapStarController.StarData sd)
	{
	    return new StarMapStarControllerImpl(smsh, sd);
	}
	
	@Override
	public ObjectSummaryController createObjectSummaryController()
	{
	    return new ObjectSummaryControllerImpl();
	}
}
