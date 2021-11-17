// IOriAidl.aidl
package com.ori.Server;

import com.ori.server.Person;

interface IOriAidl {
     void addPerson(in Person person);

     List<Person> getPsersonList();
}