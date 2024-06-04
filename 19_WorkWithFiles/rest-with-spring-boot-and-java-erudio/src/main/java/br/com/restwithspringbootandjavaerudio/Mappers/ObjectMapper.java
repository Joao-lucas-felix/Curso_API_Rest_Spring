package br.com.restwithspringbootandjavaerudio.Mappers;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapper {
    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O,D> D parseObject(O originObject, Class<D> destination){
        return mapper.map(originObject, destination );
    }
    public static <O,D> List<D> parseList(List<O> originList,
                                          Class<D> destination){
        List<D> listParsed = new ArrayList<>();
        originList.forEach((o -> listParsed.add(mapper.map(o,destination))));
        return listParsed;
    }

}
