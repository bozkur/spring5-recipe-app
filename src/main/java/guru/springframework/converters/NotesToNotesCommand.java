package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {

    @Synchronized
    @Nullable
    @Override
    public NotesCommand convert(Notes notes) {
        if(notes == null) {
            return null;
        }
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setRecipeNotes(notes.getRecipeNotes());
        notesCommand.setId(notes.getId());
        return notesCommand;
    }
}
