package net.ada;

import dev.speedslicer.Client;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;

@CTransformer(Client.class)
public class ClientTransform {
    @CInject(method = "thing", target = @CTarget("RETURN"))
    public void herz$thing (final InjectionCallback ic){
        ic.setReturnValue("overidden!");
    }
}
