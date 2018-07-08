package com.xml;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.ftl.helper.VariableType;
import com.transaction.TransactionService;

public class ScriptSetupFile extends ClassLoader {

	@SuppressWarnings("unchecked")
	public void readFile(String fileName, TransactionService trans) throws Exception {
		if (fileName == null) return;
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("resources/config/environment.xml");
		Document document = null;

		try {

			document = (Document) builder.build(xmlFile);

		} catch (JDOMException e) {
			e.printStackTrace();
		}

		Element rootNode = document.getRootElement();
		List<Element> list = rootNode.getChildren("file");
		for (Element e : list) {
			if (e.getAttributeValue("name").compareTo(fileName) == 0) {
				try {
					populate(e, trans);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void populate(Element e, TransactionService trans) throws Exception {
		Element child = e.getChild("variables");
		if (child == null)
			return;
		List<Element> list = child.getChildren("var");
		if (list == null)
			return;

		for (Element el : list) {

			switch (el.getAttributeValue("type")) {
			case "decimal":
				trans.publish(el.getAttributeValue("name"), VariableType.DECIMAL,
						Double.valueOf(el.getAttributeValue("value")));
				break;
			case "number":
				trans.publish(el.getAttributeValue("name"), VariableType.NUMBER,
						Long.valueOf(el.getAttributeValue("value")));
				break;
			case "string":
				trans.publish(el.getAttributeValue("name"), VariableType.STRING, el.getAttributeValue("value"));
				break;
			case "date":
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				trans.publish(el.getAttributeValue("name"), VariableType.DATE,
						sdf.parse(el.getAttributeValue("value")));
				break;
			case "boolean":
				trans.publish(el.getAttributeValue("name"), VariableType.BOOLEAN,
						Boolean.valueOf(el.getAttributeValue("value")));
				break;

			case "object":
				trans.publish(el.getAttributeValue("name"), VariableType.OBJECT, load(el.getAttributeValue("value")));
				break;
			case "dao":
				Object obj = load(el.getAttributeValue("value"));
				List<Element> fieldList = el.getChildren("field");
				if (fieldList != null) {
					for (Element field : fieldList) {
						loadField(obj, field.getAttributeValue("name"), field.getAttributeValue("type"),
								field.getAttributeValue("value"));
					}
				}
				publish(trans, el.getAttributeValue("name"), el.getAttributeValue("type"), obj);
				break;
			}

		}
		Element desc = e.getChild("description");

		if (desc != null) {
			trans.setDescription(desc.getAttributeValue("value"));
		}
	}

	private void publish(TransactionService trans, String name, String type, Object value) {

		switch (type) {
		case "decimal":
			trans.publish(name, VariableType.DECIMAL, value);
			break;
		case "number":
			trans.publish(name, VariableType.NUMBER, value);
			break;
		case "string":
			trans.publish(name, VariableType.STRING, value);
			break;
		case "date":
			trans.publish(name, VariableType.DATE, value);
			break;
		case "boolean":
			trans.publish(name, VariableType.BOOLEAN, value);
			break;
		case "object":
			trans.publish(name, VariableType.OBJECT, value);
			break;
		case "dao":
			trans.publish(name, VariableType.DAO, value);
			break;
		default:
			trans.publish(name, VariableType.OBJECT, value);
		}

	}

	@SuppressWarnings("resource")
	private Object load(String classname) throws Exception {
		Object result;
		String pathToClassFile = "resources/classes/";
		// Create class to hold the class to be loaded via a URL class loader
		System.out.println("Classpath: " + pathToClassFile);
		Class<?> clss = new URLClassLoader(new URL[] { new File(pathToClassFile).toURI().toURL() })
				.loadClass(classname);
		
		// Class<?> clss = new URLClassLoader(new URL[] {url}).loadClass(classname);

		result = clss.newInstance();
		return result;

	}

	private void loadField(Object obj, String fieldName, String type, String value) throws ParseException {
		String fChar;
		if (isUpper(fieldName.substring(0, 1))) {
			fChar = fieldName.substring(0, 1).toLowerCase();
		}else {
			fChar = fieldName.substring(0, 1).toUpperCase();
		}
		fieldName = "set" + fChar + fieldName.substring(1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		switch (type) {
		case "decimal":
			setField(obj, fieldName, Double.valueOf(value));
			break;
		case "number":
			setField(obj, fieldName, Long.valueOf(value));
			break;
		case "string":
			setField(obj, fieldName, value);
			break;
		case "boolean":
			setField(obj, fieldName, Boolean.valueOf(value));
			break;
		case "date":
			setField(obj, fieldName, sdf.parse(value));
			break;
		default:
			setField(obj, fieldName, value);
			break;

		}

	}
	private boolean isUpper(String ch) {
		boolean result = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(ch.substring(0, 1));
		
		return result;
	}
	public Object setField(Object obj, String method, Object... args) {
		Object o = null;
		Class<?>[] cls = null;
		Method m;
		if (args != null && args.length > 0) {
			cls = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				cls[i] = args[i].getClass();
			}
		} else {
			args = null;
		}

		try {
			if (args != null) {
				m = searchForMethod(obj.getClass(), method, args);
				if (m == null)
					throw new NoSuchMethodException("Error: '" + obj.getClass().getName() + "." + method + "()"
							+ "' does not exist or it as a wrong argument signature.");
				o = m.invoke(obj, args);
			} else {
				m = obj.getClass().getMethod(method, null);
				o = m.invoke(obj, null);
			}

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

		return o;
	}

	private boolean areTypesCompatible(Class<?>[] types, Object[] parms) {
		boolean result = true;

		if (parms != null) {
			for (int i = 0; i < types.length; i++) {
				if (ClassUtils.isAssignable(types[i], parms[i].getClass()) == false) {
					types[i] = autoBox(types[i]);
					if (ClassUtils.isAssignable(types[i], parms[i].getClass()) == false) {
						result = false;
						break;
					}
				}
			}
		}

		return result;
	}

	private Method searchForMethod(Class<?> clss, String name, Object... parms) {
		Method[] methods = clss.getMethods();
		for (int i = 0; i < methods.length; i++) {
			// Has to be named the same of course.
			if (!methods[i].getName().equals(name))
				continue;

			Class<?>[] types = methods[i].getParameterTypes();

			// Does it have the same number of arguments that we're looking for.
			if (types.length != parms.length)
				continue;

			// Check for type compatibility
			if (areTypesCompatible(types, parms))
				return methods[i];
		}
		return null;
	}

	private Class<?> autoBox(Class<?> cls) {
		Class<?> result = null;
		if (cls == double.class) {
			result = java.lang.Double.class;
		}
		if (cls == long.class) {
			result = java.lang.Long.class;
		}
		if (cls == boolean.class) {
			result = java.lang.Boolean.class;
		}
		if (cls == byte.class) {
			result = java.lang.Byte.class;
		}

		if (cls == char.class) {
			result = java.lang.Character.class;
		}

		if (cls == float.class) {
			result = java.lang.Float.class;
		}

		if (cls == int.class) {
			result = java.lang.Integer.class;
		}

		if (cls == short.class) {
			result = java.lang.Short.class;
		}

		return result;
	}

}
