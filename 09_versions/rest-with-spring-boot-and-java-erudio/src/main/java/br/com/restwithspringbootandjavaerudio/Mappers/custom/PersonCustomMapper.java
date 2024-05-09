package br.com.restwithspringbootandjavaerudio.Mappers.custom;

import br.com.restwithspringbootandjavaerudio.DataTransfers.V2.PersonDtoV2;
import br.com.restwithspringbootandjavaerudio.domain.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonCustomMapper {
    public PersonDtoV2 ConvertEntityToDto(Person person){
        PersonDtoV2 dto = new PersonDtoV2();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setBirthDay(new Date());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        return dto;
    }
    public Person ConvertDtoToEntity(PersonDtoV2 dto){
        Person person = new Person();
        person.setId(dto.getId());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        //person.setBirthDay(new Date());
        person.setAddress(dto.getAddress());
        person.setGender(dto.getGender());
        return person;
    }

}
