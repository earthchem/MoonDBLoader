package org.moondb.parser;

import java.util.ArrayList;
import java.util.List;

import org.moondb.dao.UtilityDao;
import org.moondb.model.Action;
import org.moondb.model.Actions;
import org.moondb.model.Dataset;
import org.moondb.model.Datasets;
import org.moondb.model.Method;
import org.moondb.model.Methods;
import org.moondb.model.MoonDBType;

public class ActionParser {
	public static Actions parseAction(Datasets datasets, Methods methods) {
		Actions actions = new Actions();
		List<Dataset> dsList = datasets.getDatasets();
		List<Method> methodList = methods.getMethods();
		
		ArrayList<Action> actionList = new ArrayList<Action>();
		for(Dataset ds : dsList) {
			for(Method method : methodList) {
				Action action = new Action();
				String datasetCode = ds.getDatasetCode();
				int datasetNum = UtilityDao.getDatasetNum(datasetCode);
				String methodTech = method.getMethodTechnique();
				Integer methodLabNum = method.getMethodLabNum();
				String methodComment = method.getMethodComment();
				int methodNum = UtilityDao.getMethodNum(methodTech,methodLabNum,methodComment);
				String actionName = datasetCode + " : " + methodTech;
				String actionDescription = method.getMethodComment();
				
				action.setActionDescription(actionDescription);
				action.setActionName(actionName);
				action.setActionTypeNum(MoonDBType.ACTION_TYPE_SPECIMEN_ANALYSIS.getValue()); //20: Specimen analysis
				action.setDatasetNum(datasetNum);
				action.setMethodNum(methodNum);
				
				actionList.add(action);
			}
		}
		
		actions.setActions(actionList);
		return actions;
	}
}
