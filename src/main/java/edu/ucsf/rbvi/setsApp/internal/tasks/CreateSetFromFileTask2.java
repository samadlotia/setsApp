package edu.ucsf.rbvi.setsApp.internal.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.util.ListSingleSelection;

import edu.ucsf.rbvi.setsApp.internal.CyIdType;

public class CreateSetFromFileTask2 extends AbstractTask {
	@Tunable(description="Select column to use for ID to create edge set:")
	public ListSingleSelection<String> edgesColumn = null;
	@Tunable(description="Select column to use for ID to create node set:",listenForChange="Select type:")
	public ListSingleSelection<String> nodesColumn = null;
/*	public ListSingleSelection<String> getNodesColumn() {
		if (network != null) {
			if (! type.getSelectedValue().equals("Create Node Set"))
				return new ListSingleSelection<String>(new ArrayList<String>());
			CyTable table = null;
			ListSingleSelection<String> column = null;
			table = network.getDefaultNodeTable();
			if (table != null) {
				Collection<CyColumn> cyColumns = table.getColumns();
				ArrayList<String> columnNames = new ArrayList<String>();
				columnNames.add("none");
				for (CyColumn c: cyColumns) 
					if (c.getType() == String.class) columnNames.add(c.getName());
				column = new ListSingleSelection<String>(columnNames);
				column.setSelectedValue("none");
			}
			return column;
		}
		return new ListSingleSelection<String>(new ArrayList<String>());
	}
	public void setNodesColumn(ListSingleSelection<String> c) {
		nodesColumn = c;
	} */
//	@Tunable(description="Select type (nodes/edges):")
/*	@Tunable(description="Select type:")
	public ListSingleSelection<String> type = null; */
/*	public ListSingleSelection<String> getType() {
		ArrayList<String> l = new ArrayList<String>();
		l.add("Create Node Set");
		l.add("Create Edge Set");
		return new ListSingleSelection<String>(l);
	}
	public void setType(ListSingleSelection<String> t) {
		type = t;
	} */
	@Tunable(description="Enter name for new set:")
	public String name;
	
	private SetsManager mgr;
	private CyNetwork network = null;
	private File f;
	
	public CreateSetFromFileTask2(SetsManager setsManager, CyNetworkManager cnm, File file) {
		mgr = setsManager;
		f = file;
		Set<CyNetwork> networkSet = cnm.getNetworkSet();
		for (CyNetwork net: networkSet) {
			if (net.getRow(net).get(CyNetwork.SELECTED, Boolean.class))
				network = net;
		}
		if (network != null) {
			CyTable table = null;
			table = network.getDefaultNodeTable();
			if (table != null) {
				Collection<CyColumn> cyColumns = table.getColumns();
				ArrayList<String> columnNames = new ArrayList<String>();
				columnNames.add("none");
				for (CyColumn c: cyColumns) 
					if (c.getType() == String.class) columnNames.add(c.getName());
				nodesColumn = new ListSingleSelection<String>(columnNames);
				nodesColumn.setSelectedValue("none");
			}
			table = network.getDefaultEdgeTable();
			if (table != null) {
				Collection<CyColumn> cyColumns = table.getColumns();
				ArrayList<String> columnNames = new ArrayList<String>();
				for (CyColumn c: cyColumns) 
					if (c.getType() == String.class) columnNames.add(c.getName());
				columnNames.add("none");
				edgesColumn = new ListSingleSelection<String>(columnNames);
				edgesColumn.setSelectedValue("none");
			}
		}
	/*	ArrayList<String> l = new ArrayList<String>();
		l.add("Create Node Set");
		l.add("Create Edge Set");
		type = new ListSingleSelection<String>(l); */
	}
	
	@Override
	public void run(TaskMonitor arg0) throws Exception {
		if (network != null && nodesColumn != null) {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			if (! nodesColumn.getSelectedValue().equals("none") && edgesColumn.getSelectedValue().equals("none"))
				mgr.createSetFromStream(name, nodesColumn.getSelectedValue(), network, reader, CyIdType.NODE);
			if (! edgesColumn.getSelectedValue().equals("none") && nodesColumn.getSelectedValue().equals("none"))
				mgr.createSetFromStream(name, edgesColumn.getSelectedValue(), network, reader, CyIdType.EDGE);
		}
	}

}
