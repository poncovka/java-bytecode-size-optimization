package jbyco.optimize.renaming.pool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringPool {

	Set<Entity> pool = new HashSet<Entity>();
	
	public Set<Entity> getPool() {
		return pool;
	}

	public void addType(String desc) {
		pool.add(new Entity(EntityType.TYPE, new String[]{desc}));
	}
	
	public void addField(String klass, String name, String desc) {
		pool.add(new Entity(EntityType.FIELD, new String[]{klass, name, desc}));
	}
	
	public void addMethod(String klass, String name, String desc) {
		pool.add(new Entity(EntityType.METHOD, new String[]{klass, name, desc}));
	}
	
	///////////////////////////////////////////////////////////////// Entities
	
	public enum EntityType {
		TYPE,
		FIELD,
		METHOD;
	};
	
	public class Entity {
		
		public final EntityType type;
		public final String[]   items;
		
		public Entity(EntityType type, String[] items) {
			this.type = type;
			this.items = items;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.hashCode(items);
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Entity other = (Entity) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (!Arrays.equals(items, other.items)) {
				return false;
			}
			if (type != other.type) {
				return false;
			}
			return true;
		}

		private StringPool getOuterType() {
			return StringPool.this;
		}
		
	}
	
}
