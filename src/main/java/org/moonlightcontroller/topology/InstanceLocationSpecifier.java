package org.moonlightcontroller.topology;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Implementation of ILocationSpecifier for Instance location
 *
 */
public class InstanceLocationSpecifier implements ILocationSpecifier {

	long id;

	public InstanceLocationSpecifier(long id) {
		this.id = id;
	}

	@Override
	public boolean isSingleLocation() {
		return true;
	}

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof InstanceLocationSpecifier)){
			return false;
		}
		if (obj == this){
			return true;
		}
		InstanceLocationSpecifier other = (InstanceLocationSpecifier)obj;
		if (other.id == this.id){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(17, 31).append(this.id).toHashCode();
	}
	
	@Override
	public String toString(){
		return this.id + "";
	}

	@Override
	public boolean isMatch(long m) {
        return this.id == m;
    }

	@Override
	public ILocationSpecifier findChild(long m) {
		if (this.isMatch(m)) {
			return this;
		}
		return null;
	}
}