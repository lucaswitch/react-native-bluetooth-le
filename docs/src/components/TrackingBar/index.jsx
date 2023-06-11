import React, {useEffect, useState} from 'react';

const TrackingBar = ({topicsTitles}) => {
  const [activeSection, setActiveSection] = useState('');

  useEffect(() => {
    const handleScroll = () => {
      const sections = document.querySelectorAll('.section');
      let currentSection = '';

      sections.forEach((section) => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.offsetHeight;

        if (window.scrollY >= sectionTop - 20 && window.scrollY < sectionTop + sectionHeight) {
          currentSection = section.id;
        }
      });

      setActiveSection(currentSection);
    };

    window.addEventListener('scroll', handleScroll);

    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);

  return (
    <div className="sidebar">
      <div className="fixed mt-28 right-10 h-screen sm:w-80 text-sm">
        <h4 className={'text-black ml-14'}>Nesta pagina</h4>
        <ul className="py-4 list-none">
          {topicsTitles?.map((title, index) => {
            return (
              <a href={`#section${index + 1}`}
                 className={`${activeSection === `section${index + 1}` ? 'no-underline' : 'hover:underline no-underline'}`}>
                <li key={index}
                    className={`flex flex-row py-2  ${activeSection === `section${index + 1}` 
                      ? 'bg-[#b33ce6] text-white  font-bold' 
                      : 'group text-black '}`}
                >
                  {activeSection !== `section${index + 1}` &&
                    <div className={`${activeSection !== `section${index + 1}`
                      ? 'hidden group-hover:block bg-orange-600 w-1' : 'bg-black w-1'}`}
                    />
                  }


                  <div className={'ml-6'}>
                    {title}
                  </div>
                </li>
              </a>
            );
          })}
        </ul>
      </div>
    </div>
  );
};

export default TrackingBar;