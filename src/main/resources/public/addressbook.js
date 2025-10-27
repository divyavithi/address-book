$(document).ready(function () {

    function getAddressBooks(callback) {
        $.ajax({
            url: '/addressBooks',
            method: 'GET',
            success: callback,
            error: function (xhr, status, error) {
                console.error('Error fetching AddressBooks:', error);
            }
        });
    }

    function postAddressBook(buddyList, callback) {
        $.ajax({
            url: '/addressBooks',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ buddies: buddyList }),
            success: callback,
            error: function (xhr, status, error) {
                console.error('Error creating AddressBook:', error);
            }
        });
    }

    function deleteAddressBook(bookId, callback) {
        $.ajax({
            url: '/addressBooks/' + bookId,
            method: 'DELETE',
            success: callback,
            error: function(xhr, status, error) {
                console.error('Error deleting AddressBook:', error);
            }
        });
    }

    function renderAddressBooks(addressBooks) {
        const list = $('#addressBooksList');
        list.empty();

        addressBooks.forEach(function (book) {
            const bookLi = $('<li>').text('Addressbook ' + book.id);
            const buddiesUl = $('<ul>');

            book.buddies.forEach(function (buddy) {
                buddiesUl.append(
                    $('<li>').text(buddy.name + ' - ' + buddy.phoneNumber)
                );
            });

            const deleteBtn = $('<button>')
                .text('Delete')
                .addClass('deleteBtn')
                .attr('data-id', book.id);

            bookLi.append(buddiesUl).append(deleteBtn);
            list.append(bookLi);
        });
    }

    $(document).on('click', '.deleteBtn', function () {
        const id = $(this).data('id');
        deleteAddressBook(id, function () {
            getAddressBooks(renderAddressBooks);
            location.reload();
        });
    });

    $('#addAddressBookForm').submit(function (event) {
        event.preventDefault();

        const buddyNames = $('input[name="buddyName"]').map(function () {
            return $(this).val();
        }).get();

        const buddyPhones = $('input[name="buddyPhone"]').map(function () {
            return $(this).val();
        }).get();

        const buddyList = [];

        for (let i = 0; i < buddyNames.length; i++) {
            if (buddyNames[i] && buddyPhones[i]) {
                buddyList.push({
                    name: buddyNames[i],
                    phoneNumber: buddyPhones[i]
                });
            }
        }

        if (buddyList.length === 0) {
            alert('Please provide at least one Buddy.');
            return;
        }

        postAddressBook(buddyList, function () {
            $('#addAddressBookForm')[0].reset();
            getAddressBooks(renderAddressBooks);
            location.reload();
        });
    });

    getAddressBooks(renderAddressBooks);
});
