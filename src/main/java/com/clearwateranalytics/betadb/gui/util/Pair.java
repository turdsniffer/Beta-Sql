package com.clearwateranalytics.betadb.gui.util;

/**
 * @author parmstrong
 */
public class Pair<F,S>
{
	private F first;
	private S second;

	public Pair(F first, S second)
	{
		this.first = first;
		this.second = second;
	}

	public F getFirst()
	{
		return first;
	}

	public S getSecond()
	{
		return second;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Pair<F, S> other = (Pair<F, S>) obj;
		if (this.first != other.first && (this.first == null || !this.first.equals(other.first))) return false;
		if (this.second != other.second && (this.second == null || !this.second.equals(other.second))) return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 11 * hash + (this.first != null ? this.first.hashCode() : 0);
		hash = 11 * hash + (this.second != null ? this.second.hashCode() : 0);
		return hash;
	}
	
	
}
