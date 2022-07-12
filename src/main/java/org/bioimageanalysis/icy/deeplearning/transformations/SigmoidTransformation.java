package org.bioimageanalysis.icy.deeplearning.transformations;

import java.nio.FloatBuffer;

import org.bioimageanalysis.icy.deeplearning.tensor.RaiToArray;
import org.bioimageanalysis.icy.deeplearning.tensor.Tensor;

import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;

public class SigmoidTransformation extends DefaultImageTransformation {

	public static final String name = "sigmoid";
	
	private Tensor tensor;
	
	public SigmoidTransformation(Tensor tensor) {
		this.tensor = tensor;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public Tensor apply() {
		tensor = Tensor.createCopyOfTensorInWantedDataType(tensor, new FloatType());
		float[] arr = RaiToArray.floatArray(tensor.getData());
		FloatBuffer datab = FloatBuffer.wrap(arr);
		for (int i = 0; i < arr.length; i ++) {
			datab.put(i, (float) (1.0 / ( 1.0 + Math.exp(-datab.get(i)))));
		}
		tensor.setData(ArrayImgs.floats(arr, tensor.getData().dimensionsAsLongArray()));
		return tensor;
	}
	
	public static void main(String[] args) throws InterruptedException {
		Tensor tt = Tensor.build("example", "bcyx", null);
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1; i ++) {
			SigmoidTransformation preproc = new SigmoidTransformation(tt);
			preproc.apply();
		}
		System.out.println(System.currentTimeMillis() - t1);
		System.out.println("done");
	}
}
