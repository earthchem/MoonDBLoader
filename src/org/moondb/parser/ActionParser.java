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
				String methodCode = method.getMethodTechnique();
				int methodNum = UtilityDao.getMethodNum(methodCode);
				String actionName = datasetCode + " : " + methodCode;
				String actionDescription = method.getMethodComment();
				
				action.setActionDescription(actionDescription);
				action.setActionName(actionName);
				action.setActionTypeNum(20); //20: Specimen analysis
				action.setDatasetNum(datasetNum);
				action.setMethodNum(methodNum);
				
				actionList.add(action);
			}
		}
		
		actions.setActions(actionList);
		return actions;
	}
}
