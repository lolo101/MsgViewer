Name:           MSGViewer
Version:        1.9
Release:        1%{?dist}
Summary:        Display Outlook MSG Files
Group:          Desktop
License:        GPLv3
URL:            https://sourceforge.net/p/msgviewer
BuildArch:      noarch
Source0:	MSGViewer.tar.gz

Requires:          java

%description
MsgViewer is email-viewer utility for .msg e-mail messages.

%define _binary_payload w9.gzdio

%prep
%setup -q -n %{name}

%build

%post
ln -s -f /opt/%{name}/%{name} /usr/bin/%{name}
ln -s -f /opt/%{name}/%{name} /usr/bin/msgviewer
ln -s -f /opt/%{name}/msg2mbox /usr/bin/msg2mbox
ln -s -f /opt/%{name}/msg2eml /usr/bin/msg2eml

%postun
if [ $1 -eq 0 ] ; then
	rm  /usr/bin/%{name}
	rm  /usr/bin/msgviewer
	rm  /usr/bin/msg2mbox
	rm  /usr/bin/msg2eml
fi

%install
install -d -m 755 $RPM_BUILD_ROOT/opt/%{name}/lib
install -d -m 755 $RPM_BUILD_ROOT/usr/share/applications

# jar
install -d $RPM_BUILD_ROOT/opt/%{name}
install -d $RPM_BUILD_ROOT/%{name}/lib
install -m755 MSGViewer.jar $RPM_BUILD_ROOT/opt/%{name}/%{name}.jar
install -m644 lib/*.jar $RPM_BUILD_ROOT/opt/%{name}/lib/
install -m755 %{name} $RPM_BUILD_ROOT/opt/%{name}/%{name}
install -m755 msg2mbox $RPM_BUILD_ROOT/opt/%{name}/
install -m755 msg2eml $RPM_BUILD_ROOT/opt/%{name}/
install -m644 %{name}.desktop $RPM_BUILD_ROOT/usr/share/applications
install -m644 icon.png $RPM_BUILD_ROOT/opt/%{name}

%files
/opt/%{name}/%{name}.jar
/opt/%{name}/lib/*.jar
/opt/%{name}/%{name}
/opt/%{name}/msg2mbox
/opt/%{name}/msg2eml
/opt/%{name}/icon.png
/usr/share/applications/%{name}.desktop


