package com.cowlark.stellation3.common.game;

import java.util.Arrays;
import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.controllers.LabelController;
import com.cowlark.stellation3.common.controllers.LocationController;
import com.cowlark.stellation3.common.controllers.LocationHandler;
import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.controllers.MarkupHandler;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.controllers.StarMapStarHandler;
import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.cowlark.stellation3.common.database.Database;
import com.cowlark.stellation3.common.database.RPCManager;
import com.cowlark.stellation3.common.database.Transport;
import com.cowlark.stellation3.common.model.SGalaxy;
import com.cowlark.stellation3.common.model.SPlayer;
import com.cowlark.stellation3.common.model.SStar;
import com.cowlark.stellation3.common.model.SUniverse;
import com.cowlark.stellation3.common.monitors.MonitorGroup;
import com.cowlark.stellation3.common.monitors.PaneMonitorAdaptor;
import com.cowlark.stellation3.common.monitors.summarypane.PlayerSummaryMonitor;
import com.google.gwt.user.client.Window;

public abstract class Game
{
	public static Game Instance;
	
	public Database Database;
	public Transport Transport;
	public Static Static;
	public RPCManager RPCManager;
	public SUniverse Universe;
	public SGalaxy Galaxy;
	public SPlayer Player;
	public StarMap StarMap;
	
	public void start()
	{
		Database = new Database();
		Transport = createTransport();
		Static = new Static(Transport);
		RPCManager = new RPCManager(Transport);
		
		LoginSequencer ls = new LoginSequencer();
		ls.begin();
	}
	
	public void protocolError()
	{
		Window.alert("Protocol error!");
	}
	
	public void loginCompleted(int playeroid)
	{
		Universe = (SUniverse) Database.get(1);
		Galaxy = Universe.Galaxy.<SGalaxy>get();
		Player = (SPlayer) Database.get(playeroid);
		
		StarMap = new StarMap();
		StarMap.show();
		
		MonitorGroup mg = new MonitorGroup();
		mg.addMonitor(new PlayerSummaryMonitor(Player));
		
		Pane leftPane = new PaneMonitorAdaptor(mg, PaneAspect.TITLE, null,
				"Summary");
	}
	
	public void showStarDetails(SStar star)
	{
		MonitorGroup mg = new MonitorGroup();
		Pane starPane = new PaneMonitorAdaptor(mg, PaneAspect.LOCATION, null,
				star.Name.get());
	}
	
	public abstract void loadUIData(CompletionListener listener);
	
	public abstract Transport createTransport();
	
	public abstract void showProgress(String message);
	
	public Pane showPane(Controller[] controllers,
			PaneAspect aspect, PaneHandler cgh, String title)
	{
		Pane pane = showPane(aspect, cgh, title);
		pane.updateControllers(Arrays.asList(controllers));
		return pane;
	}
	
	public abstract Pane showPane(PaneAspect aspect, PaneHandler cgh,
			String title);
	
	public abstract GroupTitleController createGroupTitleController(
			String title);
	public abstract MarkupController createMarkupController(
			MarkupHandler mh, String markup);
	public abstract LabelController createLabelController(
			String label);
	public abstract TextFieldController createTextFieldController(
			TextFieldHandler tfh, String label);
	public abstract TextFieldController createPasswordTextFieldController(
			TextFieldHandler tfh, String label);
	public abstract ButtonsController createButtonsController(
			ButtonsHandler bh, String... buttons);	
	public abstract LocationController createLocationController(
			LocationHandler lh, String label);

	public abstract StarMapStarController createStarMapStarController(
			StarMapStarHandler smsh, StarMapStarController.StarData stardata);
}
