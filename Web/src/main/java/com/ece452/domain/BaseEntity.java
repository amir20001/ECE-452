package com.ece452.domain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.ece452.util.MapperIgnore;
import com.ece452.util.TableName;

public abstract class BaseEntity {

	public void map(String prefix, ResultSet rs)
			throws UnexpectedInputException, NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, SQLException,
			InstantiationException {
		Class<? extends BaseEntity> cls = getClass();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String dbName = null;
			if (Modifier.isTransient(fields[i].getModifiers())) {
				continue;
			} else if (fields[i].getDeclaredAnnotations().length > 0) {
				Annotation[] declaredAnnotations = fields[i]
						.getDeclaredAnnotations();
				for (int j = 0; j < declaredAnnotations.length; j++) {
					Annotation annotation = declaredAnnotations[j];
					if (annotation instanceof MapperIgnore) {
						continue;
					} else if (annotation instanceof TableName) {
						TableName tablename = (TableName) annotation;
						dbName = tablename.value();
					}
				}
			} else {
				String name = fields[i].getName();
				if (dbName == null) {
					dbName = name.replaceAll("(.)([A-Z])", "$1_$2")
							.toLowerCase();
					dbName = prefix + dbName;
				}
				name = Character.toUpperCase(name.charAt(0))
						+ name.substring(1);
				String fieldType = fields[i].getType().toString().toLowerCase();
				if (fieldType.contains("int")) {
					Method method = cls.getDeclaredMethod("set" + name,
							new Class[] { Integer.TYPE });
					method.invoke(this, rs.getInt(dbName));
				} else if (fieldType.endsWith("double")) {
					Method method = cls.getDeclaredMethod("set" + name,
							new Class[] { Double.TYPE });
					method.invoke(this, rs.getDouble(dbName));
				} else if (fieldType.endsWith("long")) {
					Method method = cls.getDeclaredMethod("set" + name,
							new Class[] { Long.TYPE });
					method.invoke(this, rs.getLong(dbName));
				} else if (fieldType.endsWith("boolean")) {
					Method method = cls.getDeclaredMethod("set" + name,
							new Class[] { Boolean.TYPE });
					method.invoke(this, rs.getBoolean(dbName));
				} else if (fieldType.endsWith("string")) {
					Method method = cls.getDeclaredMethod("set" + name,
							new Class[] { String.class });
					method.invoke(this, rs.getString(dbName));
				} else if (fieldType.endsWith("timestamp")) {
					Method method = cls.getDeclaredMethod("set" + name,
							new Class[] { Timestamp.class });
					method.invoke(this, rs.getTimestamp(dbName));
				} else {
					throw new UnexpectedInputException(
							"The generic mapper does not deal with: "
									+ fieldType + " add it to BaseDao");
				}

			}
		}
	}

	public void map(ResultSet rs) throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			InstantiationException, UnexpectedInputException, SQLException {
		map("", rs);
	}
}

class UnexpectedInputException extends Exception {

	private static final long serialVersionUID = -6687341277840265375L;

	public UnexpectedInputException(String message) {
		super(message);
	}

}