package est.money.mannager.api.dtos;


import est.money.mannager.api.models.Category;

public class CategoryDto {

    public long id;
    public String name;

    public CategoryDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryDto from(Category c){
        return  new CategoryDto(c.getId(), c.getName());
    }
}
