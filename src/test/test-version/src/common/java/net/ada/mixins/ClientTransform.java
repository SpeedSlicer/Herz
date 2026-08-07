package net.ada.mixins;

import net.ada.Client;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;

@CTransformer(Client.class)
public class ClientTransform {

    @CInject(
            method = "thing",
            target = @CTarget("RETURN"),
            cancellable = true
    )
    public void herz$thing(InjectionCallback ic) {
        ic.setReturnValue("overidden!");
    }
}