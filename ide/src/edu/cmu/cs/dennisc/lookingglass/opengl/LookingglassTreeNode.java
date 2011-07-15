/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package edu.cmu.cs.dennisc.lookingglass.opengl;

import org.alice.apis.moveandturn.Transformable;
import org.alice.ide.swing.BasicTreeNode;

import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.toolkit.scenegraph.SceneGraphTreeNode;

public class LookingglassTreeNode extends BasicTreeNode{

	public float opacity;
	public boolean isShowing;
	
	public static LookingglassTreeNode createLookingglassTreeStructure( CompositeAdapter<?> lgComposite )
	{
		LookingglassTreeNode node = new LookingglassTreeNode( lgComposite );
		for ( ComponentAdapter c : lgComposite.accessComponentAdapters() )
		{
			node.add( new LookingglassTreeNode( c ) );
		}
		if ( lgComposite instanceof SceneAdapter )
		{
			for (VisualAdapter va : ((SceneAdapter) lgComposite).m_visualAdapters)
			{
				BasicTreeNode parentNode = node;
				if (va.m_element instanceof Component)
				{
					Component e = (Component)va.m_element;
					if (e.getParent() != null)
					{
						BasicTreeNode newParent = node.getMatchingNode(e.getParent().hashCode());
						if (newParent != null)
						{
							parentNode = newParent;
						}
					}
				}
				parentNode.add( new LookingglassTreeNode(va) );
			}
			for (VisualAdapter va : ((SceneAdapter) lgComposite).m_visualAdapters)
			{
				BasicTreeNode parentNode = node;
				if (va.m_element instanceof Component)
				{
					Component e = (Component)va.m_element;
					if (e.getParent() != null)
					{
						BasicTreeNode newParent = node.getMatchingNode(e.getParent().hashCode());
						if (newParent != null)
						{
							parentNode = newParent;
						}
					}
				}
				parentNode.add( new LookingglassTreeNode(va) );
			}
		}
		return node;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof LookingglassTreeNode)
		{
			return this.hashCode == ((LookingglassTreeNode)obj).hashCode;	
		}
		return super.equals(obj);
	}
	
	@Override
	protected void setData(Object object) 
	{
		super.setData(object);
		if (object instanceof ElementAdapter)
		{
			setElementAdapterBasedData((ElementAdapter)object);
		}
	}
	
	private void setElementAdapterBasedData(ElementAdapter lgElement)
	{
		if (lgElement.m_element instanceof Turnable)
		{
			edu.cmu.cs.dennisc.scenegraph.Transformable t = ((Turnable)lgElement.m_element).getSGTransformable();
			this.add(new SceneGraphTreeNode(t));
		}
		else if (lgElement.m_element instanceof edu.cmu.cs.dennisc.scenegraph.Element)
		{
			this.add(new SceneGraphTreeNode((edu.cmu.cs.dennisc.scenegraph.Element)lgElement.m_element));
		}
	}
	
	public LookingglassTreeNode(ElementAdapter lgElement)
	{
		super(lgElement);
	}
	
	public LookingglassTreeNode( VisualAdapter<Visual> lgComponent )
	{
		this((ElementAdapter)lgComponent);
		this.add( new LookingglassTreeNode( lgComponent.getFrontFacingAppearanceAdapter()) );
		if( lgComponent.m_geometryAdapters != null )
		{
			for (GeometryAdapter<?> g : lgComponent.m_geometryAdapters)
			{
				this.add( new LookingglassTreeNode(g));
			}
		}
//		this.add( new LookingglassTreeNode( lgComponent.getFrontFacingAppearanceAdapter()) );
	}
}
