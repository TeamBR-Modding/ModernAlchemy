package com.dyonovan.itemreplication.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class ModelLaserNode extends ModelBase {

    private ArrayList<ModelRenderer> bits;

    public ModelLaserNode() {
        bits = new ArrayList<ModelRenderer>();

        ModelRenderer main = new ModelRenderer(this);
        main.addBox(-5, -5, -5,
                    10, 10, 10);
        main.setRotationPoint(0, 0, 0);
        bits.add(main);
    }

    @Override
    public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float mul) {
        for(ModelRenderer part : bits) {
            part.render(mul);
        }
    }
}
