# client-server_dictionary
Общение между сервером и клиентом осуществляется путем передачи объектов. Клиент серверу отправляет команду для выполнения, а сервер отвечает строкой. Для реальной жизни луше было бы использовать иной подход, но в данном случае, нормально.

Клиент имеет две очереди: очередь исходящих и очередь входящих сообщений. Основное взаимодействие с ним осуществляется добовлением и выемкой сообщений из соответствующих очередей. В данном случае это злишество, но, на мой взгляд, так удобней работать и клиент получается более универсальным (содержит в себе только логику передачи-приема сообщений, а их обработка осуществляется отдельно). 

На сервере соединения обрабатываются немного иначе. Не стал переделывать на очереди. Там через фабрику создется обработчик соединения, который, как только получил команду, тут же ее выполняет и посылает отчет клиенту.
