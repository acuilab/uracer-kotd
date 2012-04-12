package com.bitfire.uracer.game.models;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.bitfire.uracer.Director;
import com.bitfire.uracer.utils.Convert;

public class TrackTrees {
	public final List<TreeStillModel> models;
	private final boolean owned;

	public TrackTrees( List<TreeStillModel> models, boolean owned ) {
		this.models = models;
		this.owned = owned;
	}

	public void dispose() {
		if( owned && models != null && models.size() > 0 ) {
			for( int i = 0; i < models.size(); i++ ) {
				models.get( i ).dispose();
			}

			models.clear();
		}
	}

	public int count() {
		return (models != null ? models.size() : 0);
	}

	private Vector3 tmpvec = new Vector3();
	private Matrix4 tmpmtx = new Matrix4();
	private Matrix4 tmpmtx2 = new Matrix4();

	// private Vector3[] vtrans = new Vector3[8];
	public void transform( PerspectiveCamera camPersp, OrthographicCamera camOrtho ) {
		float meshZ = -(camPersp.far - camPersp.position.z);

		for( int i = 0; i < models.size(); i++ ) {
			TreeStillModel m = models.get( i );

			Matrix4 transf = m.transformed;

			// compute position
			tmpvec.x = Convert.scaledPixels( m.positionOffsetPx.x - camOrtho.position.x ) + Director.halfViewport.x + m.positionPx.x;
			tmpvec.y = Convert.scaledPixels( m.positionOffsetPx.y + camOrtho.position.y ) + Director.halfViewport.y - m.positionPx.y;
			tmpvec.z = 1;

			// transform to world space
			camPersp.unproject( tmpvec );

			// build model matrix
			tmpmtx.setToTranslation( tmpvec.x, tmpvec.y, meshZ );
			Matrix4.mul( tmpmtx.val, tmpmtx2.setToScaling( m.scaleAxis ).val );
			Matrix4.mul( tmpmtx.val, tmpmtx2.setToRotation( m.iRotationAxis, m.iRotationAngle ).val );

			// comb = (proj * view) * model (fast mul)
			Matrix4.mul( transf.set( camPersp.combined ).val, tmpmtx.val );

			// transform the bounding box
			m.boundingBox.inf().set( m.localBoundingBox );
			m.boundingBox.mul( tmpmtx );

			// create an AABB out of the corners of the original
			// AABB transformed by the model matrix
			// bb.inf();
			// Vector3[] corners = m.localBoundingBox.getCorners();
			// for(int k = 0; k < corners.length; k++)
			// {
			// vtrans[k].x = corners[k].x;
			// vtrans[k].y = corners[k].y;
			// vtrans[k].z = corners[k].z;
			// vtrans[k].mul( tmpmtx );
			// bb.ext(vtrans[k]);
			// }
			//
			// m.boundingBox.inf();
			// m.boundingBox.set( bb );
		}
	}
}