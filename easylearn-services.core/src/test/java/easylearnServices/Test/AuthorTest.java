package easylearnServices.Test;


import easylearnServices.Core.Models.Author;
import easylearnServices.Core.Models.AuthorCollection;
import easylearnServices.Core.Test.TestBase;
import org.bson.types.ObjectId;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AuthorTest extends TestBase {

    @Test
    public void createAuthorTest() {

        Author author = _serviceContext.createAuthor("firstName", "lastName", "userName", "email");

        assertNotNull(author);
        assertNotNull(author.get_id());

        assertThat(author.get_firstName(), is("firstName"));
        assertThat(author.get_lastName(), is("lastName"));
        assertThat(author.get_userName(), is("userName"));
        assertThat(author.get_eMail(), is("email"));

        Author sameAuthor = _serviceContext.getAuthor(author.get_id());

        assertThat(author.get_id(), is(sameAuthor.get_id()));
    }

    @Test
    public void updateAuthorTest() {

        AuthorCollection authorCollection = _serviceContext.fetchAllAuthors();
        Author author = authorCollection.getElements().get(0);

        ObjectId id = author.get_id();
        author = _serviceContext.updateAuthor(id, "updatedFirstName", "updatedLastName", "updatedUserName", "updatedEmail");

        assertNotNull(author);

        assertThat(author.get_id(), is(id));
        assertThat(author.get_firstName(), is("updatedFirstName"));
        assertThat(author.get_lastName(), is("updatedLastName"));
        assertThat(author.get_userName(), is("updatedUserName"));
        assertThat(author.get_eMail(), is("updatedEmail"));
    }

    @Test
    public void fetchAllAuthorTest() {

        AuthorCollection authorCollection = _serviceContext.fetchAllAuthors();

        assertThat(authorCollection.getElements().size(), is(2));

        assertThat(authorCollection.getElements().get(0).get_firstName(), is("Frank"));
        assertThat(authorCollection.getElements().get(1).get_firstName(), is("Patrick"));
    }

    @Test
    public void deleteAuthorTest() {

        AuthorCollection authorCollection = _serviceContext.fetchAllAuthors();
        Author author = authorCollection.getElements().get(0);

        ObjectId id = author.get_id();
        _serviceContext.deleteAuthor(id);

        Author deletedAuthor = _serviceContext.getAuthor(id);

        assertNull(deletedAuthor);
    }
}
