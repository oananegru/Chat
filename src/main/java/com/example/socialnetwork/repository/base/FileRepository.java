package com.example.socialnetwork.repository.base;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.domain.validators.base.Validator;
import com.example.socialnetwork.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public abstract class FileRepository<E extends Entity> extends MemoryRepository<E> {

    private final String file;

    public FileRepository(Validator<E> validator, String file) throws IOException {
        super(validator);
        this.file = file;
        load();
    }

    @Override
    public Result add(E entity) throws IOException {
        Result result = super.add(entity);
        save();
        return result;
    }

    @Override
    public Result delete(Long id) throws IllegalArgumentException, IOException {
        Result result = super.delete(id);
        save();
        return result;
    }

    @Override
    public Result update(E entity) throws IllegalArgumentException, IOException {
        Result result = super.update(entity);
        save();
        return result;
    }

    /**
     * Load data from file
     * @throws IOException if fails loading data
     */
    private void load() throws IOException {
        File f = new File(file);
        if (f.exists()) {
            String jsonText = new String(Files.readAllBytes(f.toPath()));
            ArrayList<Object> list = new ObjectMapper().readValue(jsonText, ArrayList.class);
            ArrayList<E> entitiesList = deserialize(list);
            for (E elem : entitiesList) {
                this.entities.put(elem.getId(), elem);
            }
        }
    }

    /**
     * Save data to file
     * @throws IOException if fails saving data
     */
    private void save() throws IOException {
        // Create Json string from object
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonString = ow.writeValueAsString(entities.values());
        // Write to file
        FileWriter fw = new FileWriter(this.file);
        fw.write(jsonString);
        fw.close();
    }

    /**
     * Converts generic list tu specified type list
     * @param list - list with generic objects
     * @return converted list
     */
    protected abstract ArrayList<E> deserialize(ArrayList<Object> list);
}
