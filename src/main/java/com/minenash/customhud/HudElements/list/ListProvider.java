package com.minenash.customhud.HudElements.list;

import com.minenash.customhud.complex.ListManager;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface ListProvider {
    List<?> get();

//    default Supplier<?> supplier(ListProvider provider) {
//        return null;
////        return provider instanceof ListFunctioner<?> ? ListManager::getValue : provider::get;
//    }

    record ListFunctioner<T>(Supplier<T> supplier, Function<T,List<?>> function) implements ListProvider {
        @Override public List<?> get() {
            return function.apply(supplier.get());
        }
    }

    RequiredModmenu REGUIRES_MODMENU = new RequiredModmenu();
    class RequiredModmenu implements ListProvider {
        @Override
        public List<?> get() {
            return null;
        }
    }
}

